package com.project.team.plice.controller;

import com.project.team.plice.domain.data.TradeData;
import com.project.team.plice.dto.data.AddressDataDto;
import com.project.team.plice.dto.data.ApartDataDto;
import com.project.team.plice.dto.data.TradeDataDto;
import com.project.team.plice.dto.utils.DataUtils;
import com.project.team.plice.service.interfaces.AdminService;
import com.project.team.plice.service.interfaces.MapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MapController {

    private final AdminService adminService;
    private final MapService mapService;

    @GetMapping("/map")
    public String map(HttpServletRequest request, Authentication authentication, Model model) {
        adminService.logAccess(request, authentication);
        List<TradeData> priceDescList = mapService.findAllTradeDataOrderByPriceDesc();
        List<TradeData> priceAscList = mapService.findAllTradeDataOrderByPriceAsc();
        DataUtils dataUtils = new DataUtils();
        dataUtils.setTradeCount(priceDescList.size());
        dataUtils.setTradeMax(priceDescList.get(0));
        dataUtils.setTradeMin(priceAscList.get(0));
        model.addAttribute("dataUtils", dataUtils);
        model.addAttribute("searchKeywords", mapService.searchKeywordTop10());
        if (request.getParameter("keyword") != null) {
            model.addAttribute("keyword", request.getParameter("keyword"));
        } else if (request.getParameter("apart") != null) {
            model.addAttribute("apart", request.getParameter("apart"));
        } else if (request.getParameter("address") != null) {
            model.addAttribute("address", request.getParameter("address"));
        }
        return "layout-content/map/map";
    }

    @GetMapping("/find-data")
    @ResponseBody
    public List<Object> findData(@RequestParam(name = "region") String region, Model model) {
        List<Object> result = new ArrayList<>();
        List<ApartDataDto> apartDataList = mapService.findApartByAddressOrName(region, "");
        List<TradeData> tradeDataList = mapService.findTradeData();
        List<TradeDataDto> tradeDataDtoList = new ArrayList<>();
        for (TradeData tradeData : tradeDataList) {
            tradeDataDtoList.add(tradeData.toDto());
        }
        result.add(tradeDataList);
        result.add(apartDataList);
        return result;
    }

    @GetMapping("/find-apart")
    @ResponseBody
    public List<ApartDataDto> findApart(@RequestParam(name = "region") String region,
                                        @RequestParam(name = "apart") String apartName, Model model) {
        region = region == null ? "" : region;
        apartName = apartName == null ? "" : apartName;
        List<ApartDataDto> apartDataList = mapService.findApartByAddressOrName(region, apartName);
        return apartDataList;
    }

    @GetMapping("/map/input-search")
    public String homeSearchInput(@RequestParam(name = "inputVal") String inputVal, Model model) {
        if (inputVal != "") {
            List<AddressDataDto> addressDataList = mapService.findAddressDataByAddress(inputVal);
            List<ApartDataDto> apartDataList = mapService.findApartByAddressOrName("", inputVal);
            if (addressDataList != null) {
                addressDataList.forEach(e -> e.coincidenceHighlight(inputVal));
            }
            if (apartDataList != null) {
                apartDataList.forEach(e -> e.coincidenceHighlight(inputVal));
            }
            model.addAttribute("addressDataList", addressDataList);
            model.addAttribute("apartDataList", apartDataList);
        }
        return "layout-content/map/map :: #search-input-results";
    }

    @GetMapping("/map/keyword-save")
    public String keywordSave(@RequestParam("keyword") String keyword) {
        mapService.saveSearchKeyword(keyword);
        return "layout-content/map/map :: #trend";

    }
}
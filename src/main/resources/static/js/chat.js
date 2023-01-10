$(function () {

    $(".title-popular").click(function(){
        if(!$(this).hasClass("clicked")){
            $(".top3-wrapper .body").fadeOut(100);
            $(".lastest").css('display','none');
            $(".popular").css('display','flex');
            $(".top3-wrapper .body").fadeIn(200);
            $(".title-lastest").css('color','dimgray')
                               .removeClass("clicked");
            $(this).css('color','rgb(56, 126, 255)')
                   .addClass("clicked");
        }
    });
    
    $(".title-lastest").click(function(){
        if(!$(this).hasClass("clicked")){
            $(".top3-wrapper .body").fadeOut(100);
            $(".popular").css('display','none');
            $(".lastest").css('display','flex');
            $(".top3-wrapper .body").fadeIn(200);
            $(".title-popular").css('color','dimgray')
                               .removeClass("clicked");
            $(this).css('color','rgb(56, 126, 255)')
                   .addClass("clicked");
        }
    });

    $(".my-room").click(function(){
        $(".chat-wrapper").stop().fadeOut(200).fadeIn(300);
        $(".my-room").removeClass("active-room");
        $(this).addClass("active-room");
        $(".right-side").css({'flex-direction':'row','align-items':'center'});
        $(".top3-wrapper").hide();
        $(".top3-wrapper .head").stop().animate({'margin-bottom':'0px'},300);
        $(".top3-wrapper").css({'width':'40%','margin-top':'0px'}).show();
        $(".popular, .lastest").css('flex-direction','column');
        $(".chat-wrapper").css('display','flex');
        $(".chat").attr("data-id",$(this).data("id"));
        $(".chat").attr("data-member-phone","01012345678");
        $(".room").css({'height':'10%','margin':'20px 20px'});
        wsConnect();
    });
    $(".chat-wrapper .chat .head .close").click(function(){
        $(".my-room").removeClass("active-room");
        $(".right-side").css({'flex-direction':'column','align-items':'center'});
        $(".top3-wrapper").hide();
        $(".top3-wrapper .head").stop().animate({'margin-bottom':'40px'},300);
        $(".top3-wrapper").css({'width':'100%','margin-top':'80px'}).show();
        $(".popular, .lastest").css('flex-direction','row');
        $(".chat-wrapper").hide();
        $(".room").css({'height':'200px','margin':'0px 20px'});
    });

    $(".chat-input").keyup(function(){
        $(".chat-wrapper .regist-btn .count").text($(this).val().length + '/300');
    });

});


function wsConnect() {
    var messageInput = $('.chat-input');
    var sendBtn = $('#chat-send');
    var roomId = $('.chat').data('id');
    var memberPhone = $('.chat').data('member-phone');

    var sock = new SockJS("/ws");
    var client = Stomp.over(sock);    

    client.connect({}, function () {
        client.send('/publish/chat/in', {}, JSON.stringify({chatRoomId: roomId, phone: memberPhone})); 
        client.subscribe('/subscribe/chat/room/' + roomId, function (chat) {
            const content = JSON.parse(chat.body);
            if(content.type == 'INFO'){
                let messagebox = document.createElement("div");
                messagebox.setAttribute('class','info-wrapper');
                messagebox.innerHTML = content.message;
                document.getElementById("chat-box").appendChild(messagebox);
            } else {
                let hour = new Date(content.regDate).getHours();
                hour = hour <= 12 ? '오전 ' + hour : '오후' + (Number(hour) - 12);
                let minute = new Date(content.regDate).getMinutes();
                minute = minute < 10 ? '0' + minute : minute;
                let messagebox = document.createElement("div");
                messagebox.setAttribute('class','content-wrapper');
                messagebox.innerHTML = 
                "<div class='profile-wrapper'>"
                    + "<img src='" + content.member.profileImgPath + "'>"
                + "</div>"
                + "<div class='content'>"
                    + "<strong>" + content.member.nickname + "</strong>"
                    + content.message
                    + "<div class='date'>" + hour + ":" + minute + "</div>"
                + "</div>"
                document.getElementById("chat-box").appendChild(messagebox);
            }
        });
    });

    sendBtn.click(function () {
        var message = messageInput.val();
        client.send('/publish/chat/message', {}, JSON.stringify({chatRoomId: roomId, message: message, phone: memberPhone}));
        messageInput.val('');
        $(".chat-wrapper .regist-btn .count").text('0/300');
    });
}
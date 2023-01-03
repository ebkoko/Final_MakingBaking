$(function() {
	$("#btn-login").on('click', function() {
		$('.login-form-container').toggleClass('active');
	});
	
	$('#close-login-form').on('click', function() {
		$('.login-form-container').removeClass('active');
	});
	
	let pswrd = document.querySelector('#pw3');
    let show = document.querySelector('.show');
    show.onclick = function() {
      if(pswrd.type === 'password') {
        pswrd.setAttribute('type', 'text');
        show.classList.add('hide');
      }
      else {
        pswrd.setAttribute('type', 'password');
        show.classList.remove('hide');
      }
    }
    
	console.log($("#errorMsg").val());
	
	const url = new URL(window.location.href);
	
	const urlParams = url.searchParams;
	
	if(urlParams.get("error")) {
		alert(urlParams.get("errorMsg"));
	}
	
	if(urlParams.get("loginPage")) {
		$('.login-form-container').toggleClass('active');
	}
		/*
	$("#btnLogin").on("click", function() {
		console.log($("#loginForm").serialize());
		$.ajax({
			url: '/main/login',
			type: 'post',
			data: $("#loginForm").serialize(),//name=val
			success: function(obj) {
				console.log(obj);
				if(obj.item.msg == "idFail") {
					alert("존재하지 않는 아이디입니다.");
					return;
				} else if(obj.item.msg == "pwFail") {
					alert("비밀번호가 틀렸습니다.");
					return;
				}
				
				window.location.href="/";
			},
			error: function(e) {
				console.log(e);
			}
		})
	});
	*/
	// 비회원 클릭시 뜨는 창
	$("#mypage").on("click", function(e) {
		const isAuthenticated = $(".header > input[name='authenticate']").val();
		if(isAuthenticated == "false") {
			e.preventDefault();
			$('.login-form-container').toggleClass('active');
		}
	});
});
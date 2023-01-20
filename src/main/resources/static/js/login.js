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
		$("#btn-login").click();
		alert(urlParams.get("errorMsg"));
		location.href="/main/main/msg=joinSuccess";
	}
	
	if(urlParams.get("loginPage")) {
		$("#btn-login").click();
		return false;
	}
	
	if(urlParams.get("loginPage")) {
		$('.login-form-container').toggleClass('active');
	}
	
	// 비회원 클릭시 뜨는 창
	$("#mypage").on("click", function(e) {
		const isAuthenticated = $(".header > input[name='authenticate']").val();
		if(isAuthenticated == "false") {
			e.preventDefault();
			$('.login-form-container').toggleClass('active');
		}
	});
	
	if($("#orderMsg").val() != null && $("#orderMsg").val() != "") {
		if($("#orderMsg").val() == 'fail') {
			alert("결제에 실패하였습니다.");
		} else if($("#orderMsg").val() == 'cancel') {
			alert("결제가 취소되었습니다.");
		}
	}
});
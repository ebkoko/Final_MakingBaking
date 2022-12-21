$(function() {
	$("#btn-login").on('click', function() {
		$('.login-form-container').toggleClass('active');
	});
	
	$('#close-login-form').on('click', function() {
		$('.login-form-container').removeClass('active');
	});
	
	let pswrd = document.querySelector('#pw');
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
});
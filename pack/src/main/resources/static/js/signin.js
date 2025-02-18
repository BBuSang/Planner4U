let isEmailAvailable = false; // 이메일 중복 확인 상태를 저장하는 변수

// 이메일 중복 검사 함수
function checkEmail() {
    const email = document.getElementById('email').value;
    const emailCheckButton = document.querySelector('.email-check-button');

    // 이메일이 비어있는 경우
    if (!email) {
        alert("이메일을 입력하세요.");
        return;
    }

    const params = new URLSearchParams();
    params.append('email', email);

    fetch(`/api/signin/email`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: params.toString()
    })
    .then(response => response.text())
    .then(result => {
        if (result === "Email already exists") {
            alert("이메일이 이미 존재합니다.");
            isEmailAvailable = false;
        } else if(result === "Email is available") {
            alert("이메일 사용 가능");
            isEmailAvailable = true;
        }
        checkSignupButtonState(); // 이메일 중복 확인 후 버튼 상태를 다시 체크
    })
    .catch(error => {
        console.error("중복 검사 중 오류 발생:", error);
        alert("중복 검사에 실패했습니다.");
    });
}

// 비밀번호 확인 및 폼 제출 함수
function validateForm() {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm-password').value;
    
    if (password !== confirmPassword) {
        alert("비밀번호가 일치하지 않습니다");
        return false; // 폼 제출 방지
    }
    return true; // 폼 제출 허용
}

// 회원가입 버튼 활성화 상태를 체크하는 함수
function checkSignupButtonState() {
    const email = document.getElementById('email').value.trim();
    const name = document.getElementById('name').value.trim();
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm-password').value;
    const signupButton = document.getElementById('signup-button');

    // 이메일 중복 체크가 완료되었고, 이름이 입력되어 있으며, 비밀번호가 일치하는지 확인
    if (isEmailAvailable && name && password && confirmPassword && password === confirmPassword) {
        signupButton.disabled = false;
    } else {
        signupButton.disabled = true;
    }
}

function formsignin(){
	alert("회원가입이 완료되었습니다");
}


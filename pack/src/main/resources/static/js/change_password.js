let isEmailAvailable = false; // 이메일 중복 확인 상태를 저장하는 변수

// 이메일 중복 검사 함수
async function checkEmail() {
    const email = document.getElementById('email').value.trim();
    const newPasswordInput = document.getElementById('new-password');
    const confirmPasswordInput = document.getElementById('confirm-password');
    const changePasswordButton = document.getElementById('change-password-button');

    if (!email) {
        alert("이메일을 입력하세요.");
        return;
    }

    const params = new URLSearchParams();
    params.append('email', email);

    try {
        const response = await fetch('/api/pw/check-email', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: params.toString()
        });

        const result = await response.text();

        if (response.status === 200) {
            if (result === "you can change") {
                alert("이메일 확인되었습니다.");
                isEmailAvailable = true;
                newPasswordInput.disabled = false;
                confirmPasswordInput.disabled = false;
                changePasswordButton.disabled = false;
            } else {
                alert("서버에서 예기치 않은 응답을 받았습니다.");
                isEmailAvailable = false;
                newPasswordInput.disabled = true;
                confirmPasswordInput.disabled = true;
                changePasswordButton.disabled = true;
            }
        } else if (response.status === 403) {
            alert("변경할 수 없는 이메일입니다.");
            isEmailAvailable = false;
            newPasswordInput.disabled = true;
            confirmPasswordInput.disabled = true;
            changePasswordButton.disabled = true;
        } else if (response.status === 404) {
            alert("이메일을 찾을 수 없습니다.");
            isEmailAvailable = false;
            newPasswordInput.disabled = true;
            confirmPasswordInput.disabled = true;
            changePasswordButton.disabled = true;
        } else {
            alert("서버에서 응답을 받을 수 없습니다.");
        }
    } catch (error) {
        console.error("확인 중 오류 발생:", error);
        alert("이메일 검색에 실패했습니다.");
    }
}

// 비밀번호 확인 및 버튼 활성화 상태를 체크하는 함수
function checkPasswordButtonState() {
    const newPassword = document.getElementById('new-password').value;
    const confirmPassword = document.getElementById('confirm-password').value;
    const changePasswordButton = document.getElementById('change-password-button');

    if (newPassword && confirmPassword && newPassword === confirmPassword) {
        changePasswordButton.disabled = false;
    } else {
        changePasswordButton.disabled = true;
    }
}

// 폼 유효성 검사
function validateForm() {
    const newPassword = document.getElementById('new-password').value;
    const confirmPassword = document.getElementById('confirm-password').value;

    if (newPassword !== confirmPassword) {
        alert("비밀번호가 일치하지 않습니다.");
        return false; // 폼 제출 방지
    }
    return true; // 폼 제출 허용
}

// 이벤트 리스너 추가
document.addEventListener('DOMContentLoaded', () => {
    document.querySelector('.email-check-button').addEventListener('click', checkEmail);
    document.getElementById('new-password').addEventListener('input', checkPasswordButtonState);
    document.getElementById('confirm-password').addEventListener('input', checkPasswordButtonState);
    document.querySelector('form').addEventListener('submit', validateForm);
});

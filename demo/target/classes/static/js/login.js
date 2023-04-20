const login = document.getElementById('login');
const pass = document.getElementById('password');

const btn = document.getElementById('btn');



btn.addEventListener("click", () => {

    let user = {
        login: login.value,
        password: pass.value
    };
    (async() => {


        let response = await fetch('/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(user)
        });

        if (response.ok) {
            let jsonRes = await response.json();
            console.log(jsonRes);
            document.cookie = "TOKEN=" + jsonRes.token;

            document.getElementById('profile').click();

        } else {
            alert("Ошибка HTTP: ");
        }

    })()


});
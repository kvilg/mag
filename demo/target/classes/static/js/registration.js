const login = document.getElementById('login');
const nameUser = document.getElementById('name');
const pass1 = document.getElementById('password1');
const pass2 = document.getElementById('password2');

const btn = document.getElementById('btn');

btn.addEventListener("click", () => {

    if (pass1.value != pass2.value) {
        return;
    }



    let user = {
        login: login.value,
        name: nameUser.value,
        password: pass1.value
    };
    (async() => {


        let response = await fetch('/auth/registration', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(user)
        });

        if (response.ok) {


            document.querySelector('.text-url').click();

        } else {
            alert("Ошибка HTTP: ");
        }

    })()


});
<#ftl encoding = 'UTF-8'>
<#import "spring.ftl" as spring/>
<@spring.bind "model"/>

<body>
<form action="/sign_up?id=${model.id}" method="post">
    <center>
        <div id="container">
            <div id="login container">
                <p class="label">Телефон: ${model.phone}</p>
                <p><input type="text" placeholder="Введите текст из смс" name="smsCode" class="form"></p>
                <p><input type="text" placeholder="Введите имя" name="firstName" class="form"></p>
                <p><input type="text" placeholder="Введите Фамилию" name="secondName" class="form"></p>
                <p><input type="submit" class="button"></p>
                <p><a href="/accounts">Добавить другой аккаунт</a></p>

            </div>
        </div>
    </center>
</form>
</body>
<#ftl encoding = 'UTF-8'>
<#import "spring.ftl" as spring/>
<@spring.bind "model"/>
<#include "header.ftl">
<body>
<form action="/sign_in?id=${model.id}" method="post">
    <center>
        <div id="container">
            <div id="login container">
                <h1>Подтверждение</h1>
                <p class="label">Телефон: ${model.phone}</p>
                <p><input type="text" placeholder="Введите текст из смс" name="smsCode" class="form"></p>
                <p><input type="submit" class="button"></p>
                <br>
                <p><a href="/">Домой</a></p>
            </div>
        </div>
    </center>
</form>

</body>
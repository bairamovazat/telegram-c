<#ftl encoding = 'UTF-8'>
<#import "spring.ftl" as spring/>
<@spring.bind "model"/>
<#include "header.ftl">
<body>
<form action="/accounts" method="post">
    <center>
        <div id="container">
            <div id="login container">
                <h1>Добавление аккаунта</h1>
                <p class="label">Ведите телефон:</p>
                <p><input type="text" placeholder="Телефон" name="phone" class="form"></p>
                <p><input type="submit" class="button"></p>
                <p><a href="/">Домой</a></p>
            </div>
        </div>
    </center>
</form>
</body>
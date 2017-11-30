<#ftl encoding = 'UTF-8'>
<#import "spring.ftl" as spring/>
<@spring.bind "model"/>
<#include "header.ftl">
<body>
<center>
    <div id="container">
        <div id="login container">
            <p class="label"><a href="/accounts">Добавить аккаун в базу данных</a></p>
            <p class="label"><a href="/channels">Добавить подписчика в канал</a></p>
            <p class="label" alt = "Освобождает занимаемую ими память"><a href="/close_all_api">Очистить все процессы верификации, ожидающие подтверждения</a></p>
        </div>
    </div>
</center>
</body>
<#ftl encoding = 'UTF-8'>
<#import "spring.ftl" as spring/>
<@spring.bind "model"/>

<body>
Ошибка: ${model.error}
<br>
<a href="/accounts">Добавить другой аккаунт</a>
</body>
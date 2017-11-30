<#ftl encoding = 'UTF-8'>
<#import "spring.ftl" as spring/>
<@spring.bind "model"/>
<#include "header.ftl">
<body>
    <center>
        <div id="container">
            <div id="login container">
                <h1>Ошибка</h1>
                <p class="label">${model.error}</p>
                <p><a href="/">Домой</a></p>
            </div>
        </div>
    </center>

</body>
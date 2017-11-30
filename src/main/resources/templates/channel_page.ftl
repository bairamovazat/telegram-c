<#ftl encoding = 'UTF-8'>
<#import "spring.ftl" as spring/>
<@spring.bind "model"/>
<#include "header.ftl">
<body>
<form action="/channels" method="post">
    <center>
        <div id="container">
            <div id="login container">
            <table class = "table">
                <tr>
                    <th class="label">Выбрать</th><th class="label" >Номер</th>
                </tr>

                <#list model.sessions as session>
                    <tr>
                        <td><input name="id" type="radio" value="${session.getId()}">${session.getFirstName()}</td>
                        <td>${session.getPhone()}</td>
                    </tr>
                </#list>

            </table>
                <p><input type="text" name="channelName" placeholder="Введите название канала, на который хотите подписаться" class="label"></p>
                <p><input type="submit" class="button"></p>
            </div>
            <p><a href="/">Домой</a></p>
        </div>
    </center>
</form>
</body>
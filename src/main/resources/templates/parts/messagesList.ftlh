<#include "security.ftlh">

<div class="card-columns" id="message-list"> <#-- id для тестов, xpath-->
    <#list messages as message>
        <div class="card my-3" data-id="${message.id}"> <#-- data-id=""${message.id} - уник id выводим для тестирования-->
            <#if message.filename??>
                <img alt="uploading..." src="/img/${message.filename}" class="card-img-top" />
            </#if>
            <div class="m-2">
                <span>${message.text}</span><br/>
                <i>#${message.tag}</i>
            </div>
            <div class="card-footer text-muted">
               <a href="/user-messages/${message.author.id}"> ${message.authorName}</a> <#--Позволит видеть сообщения конкретного пользователя-->

                <#--редактировать собственные месседжи-->
                <#if message.author.id == currentUserId>
                <a class="btn btn-primary"   href="/user-messages/${message.author.id}?message=${message.id}">
                Edit
                </a>
                </#if>
            </div>
        </div>
    <#else>
        No message
    </#list>
</div>
<#include "security.ftlh">
<#import "pager.ftlh" as p>

<@p.pager url page />
<div class="card-columns" id="message-list"> <#-- id для тестов, xpath-->

    <#list page.content as message>
        <div class="card my-3"
             data-id="${message.id}"> <#-- data-id=""${message.id} - уник id выводим для тестирования-->
            <#if message.filename??>
                <img alt="uploading..." src="/img/${message.filename}" class="card-img-top"/>
            </#if>
            <div class="m-2">
                <span>${message.text}</span><br/>
                <i>#${message.tag}</i>
            </div>
            <div class="card-footer text-muted container">
                <div class="row">
                    <a class="col align-self-center" href="/user-messages/${message.author.id}"> ${message.authorName}</a> <#--Позволит видеть сообщения конкретного пользователя-->
                    <a class="col align-self-center" href="/messages/${message.id}/like">
<#--                        Лайки - картинка-->
                        <#if message.meLiked>
                        <i class="fas fa-heart"></i>
                            <#else>
                        <i class="far fa-heart"></i>
                        </#if>
                        ${message.likes}
                    </a>
                    <#--редактировать собственные месседжи-->
                    <#if message.author.id == currentUserId>
                        <a class="col btn btn-primary" href="/user-messages/${message.author.id}?message=${message.id}">
                            Edit
                        </a>
                    </#if>
                </div>
            </div>
        </div>
    <#else>
        No message

    </#list>
</div>
<@p.pager url page />
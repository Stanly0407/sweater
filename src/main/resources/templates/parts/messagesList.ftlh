
<div class="card-columns">
    <#list messages as message>
        <div class="card my-3" >
            <#if message.filename??>
                <img alt="uploading..." src="/img/${message.filename}" class="card-img-top">
            </#if>
            <div class="m-2">
                <span>${message.text}</span><br/>
                <i>#${message.tag}</i>
            </div>
            <div class="card-footer text-muted">
               <a href="/user-messages/${message.author.id}"> ${message.authorName}</a> <#--Позволит видеть сообщения конкретного пользователя-->
                <a href="/user-messages/${message.author.id}"> ${message.authorName}</a> <#--редактировать собственные мессенджи-->
            </div>
        </div>
    <#else>
        No message
    </#list>
</div>
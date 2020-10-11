<#import "parts/common.ftlh" as c>

<@c.page>
<#if isCurrentUser>  <#--   Будет отображаться только на странице текущ.юзера. не на чужой-->
    <#include "parts/messagesEdit.ftlh" />
</#if>
    <#include "parts/messagesList.ftlh" />

</@c.page>
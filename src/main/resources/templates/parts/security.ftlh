<#assign
known = Session.SPRING_SECURITY_CONTEXT??>
<#--?? - Приводит к булевскому, если в конце поставить-->

<#if known>
    <#assign
    user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    name = user.getUsername()
    isAdmin = user.isAdmin()
    >
<#else>
    <#assign
    name = "Guest"
    isAdmin = false
    >

</#if>

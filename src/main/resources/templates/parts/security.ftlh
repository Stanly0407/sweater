<#assign
    known = Session.SPRING_sECURITY_CONTEXT??>
<#--?? - Приводит к булевскому, если в конце поставить-->

<#if known>
    <#assign
    user = Session.SPRING_sECURITY_CONTEXT.authentication.principal

        >
    <#else >


</#if>

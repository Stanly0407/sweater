<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>

<@c.page>
Registrarion:
${message!""}
    <@l.login "/registration" />
</@c.page>
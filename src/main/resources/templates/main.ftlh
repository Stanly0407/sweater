<#import "parts/common.ftlh" as c>

<@c.page>
<#--    Панель поиска-->
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/main" class="form-inline">
                <input type="text" name="filter" class="form-control" value="${filter!""}" placeholder="Search by tag">
                <button type="submit" class="btn btn-primary ml-2">Search</button>
            </form>
        </div>
    </div>

<#--    Страничка по добавлению месседжа-->

    <#include "parts/messagesEdit.ftlh" />
    <#include "parts/messagesList.ftlh" />

</@c.page>
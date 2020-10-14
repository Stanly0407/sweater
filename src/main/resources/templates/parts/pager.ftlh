<#macro pager url page>
    <div class="mt-3">
        <ul class="pagination">
            <li class="page-item disabled">
                <a class="page-link" href="#" tabindex="-1">Page</a>
            </li>
            <#list 1..page.getTotalPages() as p>
<#--                проверка чтобы ссылка была не на активную страницу:  -->
                <#if (p - 1) == page.getNumber()> <#-- -1 = чтобы нулевая страница отображалась-->
                <li class="page-item active">
                    <a class="page-link" href="#" tabindex="-1">${p}</a>
                </li>
                <#else>
                    <li class="page-item">
                        <a class="page-link" href="${url}?page=${p}" tabindex="-1">${p}</a>
                    </li>
                </#if>
            </#list>
            </ul>

        <ul class="pagination">
            <li class="page-item disabled">
                <a class="page-link" href="#" tabindex="-1">Page element</a>
            </li>
            <#list [5, 10, 25, 50] as с> <#--     Количество элементов на странице-->


                <#if с == page.getSize()>
                    <li class="page-item active">
                        <a class="page-link" href="#" tabindex="-1">${p}</a>
                    </li>
                <#else>
                    <li class="page-item">
                        <a class="page-link" href="${url}?page=${p}" tabindex="-1">${p}</a>
                    </li>
                </#if>
            </#list>
        </ul>

    </div>

</#macro>
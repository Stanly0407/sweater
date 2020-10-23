<#macro page>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>Sweater</title>
        <link rel="stylesheet" href="/static/style.css" />

        <#--сообщаем боаузеру, что он должен учитывать плотность пикселей на устройстве-->
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />

<#--        иконки-->
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css" integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU" crossorigin="anonymous">
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
              integrity="sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z"
              crossorigin="anonymous" />
<#--        вставляем шаблон (подключаем) reCAPTCHA - чтоб отобразить виджет reCAPTCHA на вашей странице-->
        <script src='https://www.google.com/recaptcha/api.js'></script>
<#--        turbolinks - для ускорения отобюражения страниц-->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/turbolinks/5.2.0/turbolinks.js"></script>
        <!-- Optional JavaScript -->
        <!-- jQuery first, then Popper.js (отрисовывает всплывающие тул типы, then Bootstrap JS (подключает плагины для функциональности) -->
        <script defer crossorigin="anonymous"
                integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
                src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script defer src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"
                integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN"
                crossorigin="anonymous"></script>
        <style>
            .turbolinks-progress-bar {
                height: 2px;
                background-color: navy;
            }
        </style>
    </head>
    <body>

<#--    для помещения navbar в шабон-->
<#include "navbar.ftlh">
    <div class="container mt-5"> <#-- контент выравнивается с отступом-->
        <#nested>
    </div>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"
            integrity="sha384-B4gt1jrGC7Jh4AgTPSdUtOBvfO8shuf57BaghqFfPlYxofvL8/KUEfYiJOMMV+rV"
            crossorigin="anonymous"></script>
    </body>
    </html>
</#macro>
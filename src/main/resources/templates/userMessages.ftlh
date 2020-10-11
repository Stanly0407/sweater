<#import "parts/common.ftlh" as c>

<@c.page>
<#--    чья страничка-->
    <h3> ${userChannel.username}</h3>
<#--    Кнопка подписки, отображаем только если мы пришли не на свой канал-->
    <#if !isCurrentUser>
    <#--    Возможность отписки  -->
        <#if isSubscriber>
            <a class="btn btn-info" href="/user/unsubscribe/${userChannel.id}">Unsubscribe</a>
        <#else>
            <a class="btn btn-info" href="/user/subscribe/${userChannel.id}">Subscribe</a>
        </#if>
    </#if>
    <div class="container my-3">
        <div class="row">
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="card-title">Subscriptions</div>
                        <h3 class="card-text">
                            <a href="/user/subscriptions/${userChannel.id}/list">${subscriptionsCount}</a>
                        </h3>
                    </div>
                </div>
            </div>

            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="card-title">Subscribers</div>
                        <h3 class="card-text">
                            <a href="/user/subscribers/${userChannel.id}/list">${subscribersCount}</a>
                        </h3>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <#if isCurrentUser>  <#--   Будет отображаться только на странице текущ.юзера. не на чужой-->
        <#include "parts/messagesEdit.ftlh" />
    </#if>
    <#include "parts/messagesList.ftlh" />

</@c.page>
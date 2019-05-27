<#include "../include/imports.ftl">

<div>
  <h1>Article List Page</h1>
  <h4>${document.title}</h4>
  ${document.overview}
  <#list articleLinks as articleName, articleLink>
      <div>
          <a href="${articleLink}">${articleName}</a>
      </div>
  </#list>
</div>
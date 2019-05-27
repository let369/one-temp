<#include "../include/imports.ftl">

<div>
  <h1>Category Page</h1>
  <h4>${document.title}</h4>
  ${document.overview}
  <#list folderLinks as folderName, folderLink>
      <a href="${folderLink}">${folderName}</a>
  </#list>
</div>
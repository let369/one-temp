package org.example.components;

import java.util.LinkedHashMap;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.linking.HstLink;
import org.onehippo.cms7.essentials.components.EssentialsContentComponent;

public class ArticleListPageComponent extends EssentialsContentComponent {
    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        String relativePath = request.getRequestContext().getResolvedSiteMapItem().getRelativeContentPath();
        String siteContentPath = request.getRequestContext().getSiteContentBaseBean().getPath();
        LinkedHashMap<String,String> articleLinks = new LinkedHashMap<>();
        try {
            Node siteContentNode = request.getRequestContext().getSession().getNode(siteContentPath);
            Node relativeNode = siteContentNode.getNode(relativePath);
            NodeIterator nextFoldersIter = null;
            if(relativeNode.isNodeType("hippo:handle")){
                nextFoldersIter  = relativeNode.getParent().getNodes();
            } else if (relativeNode.isNodeType("hippostd:folder")){
                nextFoldersIter = relativeNode.getNodes();
            }

            getAllArticleLinks(request, nextFoldersIter, articleLinks);

        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        if(!articleLinks.isEmpty()){
            request.setAttribute("articleLinks",articleLinks);
        }
    }

    private void getAllArticleLinks(HstRequest request, NodeIterator nodeIterator, LinkedHashMap<String,String> articleLinks) throws RepositoryException {
        while(nodeIterator.hasNext()){
            Node nextSibling = nodeIterator.nextNode();
            if(nextSibling.isNodeType("hippostd:folder")) {
                NodeIterator folderChildren = nextSibling.getNodes();
                getAllArticleLinks(request, folderChildren, articleLinks);
            } else if (nextSibling.getNode(nextSibling.getName()).isNodeType("myproject:article")){
                HstLink folderLink = request.getRequestContext().getHstLinkCreator().create(nextSibling, request.getRequestContext().getResolvedMount().getMount());
                String folderUrlString = folderLink.toUrlForm(request.getRequestContext(),true);
                articleLinks.put(nextSibling.getName(),folderUrlString);
            }
        }
    }
}

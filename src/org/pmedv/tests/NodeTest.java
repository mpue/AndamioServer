/**

	WeberknechtCMS - Open Source Content Management
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2009 Matthias Pueski
	
	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/
package org.pmedv.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pmedv.beans.objects.NodeBean;
import org.pmedv.context.AppContext;
import org.pmedv.pojos.Node;
import org.pmedv.services.NodeService;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/WEB-INF/applicationContext.xml"})

public class NodeTest {
	
	@Test
	public void testService() {

		ApplicationContext ctx = AppContext.getApplicationContext();    
	    assertNotNull(ctx);
	    
	    NodeService service = (NodeService)ctx.getBean("nodeService");
	    assertNotNull(service);
		
	}
	
	@Test
	public void testFindAll() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();    
	    assertNotNull(ctx);
	    
	    NodeService service = (NodeService)ctx.getBean("nodeService");
	    assertNotNull(service);
	    
	    ArrayList<NodeBean> nodes = service.findAllRootNodes();

	    for (NodeBean node : nodes) {
	    	System.out.println(node.getName());
	    	printChildren(node);
	    }
	    
	    
	}

	private void printChildren(NodeBean node) {
		
		if (node.getChildren() != null) {
		
			for (NodeBean child : node.getChildren()) {
				System.out.println("child :"+child.getName());				
				printChildren(child);
			}
			
		}
		
		
		
	}
	
	@Test
	public void testCreateFindAndDelete() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();    
	    assertNotNull(ctx);

	    NodeService service = (NodeService)ctx.getBean("nodeService");
	    assertNotNull(service);
	    
	    NodeBean nodeBean = new NodeBean();
	    
	    nodeBean.setName("Test01");
	    nodeBean.setParent(null);
	    nodeBean.setFirstchild(false);
	    nodeBean.setImage("Test01");
	    nodeBean.setPluginid(null);
	    nodeBean.setPluginparams(null);
	    nodeBean.setType(Node.TYPE_CONTENT);
	    nodeBean.setPosition(0);

	    assertEquals(Long.valueOf(1), service.createNode(nodeBean, null));

	    NodeBean checkNode = service.findNodeByName("Test01");
	    
	    assertEquals(nodeBean.getName(), checkNode.getName());
	    assertEquals(nodeBean.getParent(), checkNode.getParent());
	    assertEquals(nodeBean.getFirstchild(), checkNode.getFirstchild());
	    assertEquals(nodeBean.getImage(), checkNode.getImage());
	    assertEquals(nodeBean.getPluginid(), checkNode.getPluginid());
	    assertEquals(nodeBean.getPluginparams(), checkNode.getPluginparams());
	    assertEquals(nodeBean.getType(), checkNode.getType());
	    assertEquals(nodeBean.getPosition(),checkNode.getPosition());
	    
	    checkNode.setRemoteId(checkNode.getId());
	    
	    assertEquals(true, service.deleteNode(checkNode));
	    
	    NodeBean deletedNode = service.findNodeByName("Test01");
	    
	    assertNull(deletedNode);
	    
	}
	
	@Test
	public void updateNodeTest() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();    
	    assertNotNull(ctx);

	    NodeService service = (NodeService)ctx.getBean("nodeService");
	    assertNotNull(service);
		
	    NodeBean nodeBean = new NodeBean();
	    
	    nodeBean.setName("Test01");
	    nodeBean.setParent(null);
	    nodeBean.setFirstchild(false);
	    nodeBean.setImage("Test01");
	    nodeBean.setPluginid(null);
	    nodeBean.setPluginparams(null);
	    nodeBean.setType(Node.TYPE_CONTENT);
	    nodeBean.setPosition(0);

	    assertNotNull(service.createNode(nodeBean, null));
	    NodeBean checkNode = service.findNodeByName("Test01");				
	    assertNotNull(checkNode);
	    
	    checkNode.setName("Test01_changed");
	    
	    checkNode.setRemoteId(checkNode.getId());
	    
	    assertEquals(true,service.updateNode(checkNode));
	    
	    NodeBean changedNode = service.findNodeByName("Test01_changed");
	    
	    assertNotNull(changedNode);	    
	    assertEquals("Test01_changed", changedNode.getName());
	    
	    changedNode.setRemoteId(changedNode.getId());
	    assertEquals(true, service.deleteNode(changedNode));	    
	    NodeBean deletedNode = service.findNodeByName("Test01_changed");	    
	    assertNull(deletedNode);
	    
	}

	@Test
	public void createAndUpdateChildNodeTest() {
		
	    ApplicationContext ctx = AppContext.getApplicationContext();    
	    assertNotNull(ctx);

	    NodeService service = (NodeService)ctx.getBean("nodeService");
	    assertNotNull(service);
		
	    NodeBean nodeBean = new NodeBean();
	    
	    nodeBean.setName("Test01");
	    nodeBean.setParent(null);
	    nodeBean.setFirstchild(false);
	    nodeBean.setImage("Test01");
	    nodeBean.setPluginid(null);
	    nodeBean.setPluginparams(null);
	    nodeBean.setType(Node.TYPE_CONTENT);
	    nodeBean.setPosition(0);

	    assertNotNull(service.createNode(nodeBean, null));
	    NodeBean checkNode = service.findNodeByName("Test01");				
	    assertNotNull(checkNode);
	    checkNode.setRemoteId(checkNode.getId());
	    
	    NodeBean childNode = new NodeBean();
	    
	    childNode.setName("Child01");
	    childNode.setParent(checkNode);
	    
	    checkNode.setRemoteId(checkNode.getId());
	    
	    assertNotNull(service.createNode(childNode, checkNode));
	    
	    NodeBean changedChildNode = service.findNodeByName("Child01");

	    changedChildNode.setName("ChangedChild01");
	    
	    System.out.println("Parent id : "+changedChildNode.getParent().getId());
	    
	    
	    changedChildNode.setRemoteId(changedChildNode.getId());
	    changedChildNode.getParent().setRemoteId(changedChildNode.getParent().getId());
	    
	    assertEquals(true,service.updateNode(changedChildNode));
	    
	    NodeBean nodeWithChild = service.findNodeByName("Test01");
	    assertNotNull(nodeWithChild);
	    
	    assertEquals(1, nodeWithChild.getChildren().size());
	    
	    NodeBean child = nodeWithChild.getChildren().get(0);	    
	    assertEquals("ChangedChild01", child.getName());

	    nodeWithChild.setRemoteId(nodeWithChild.getId());
	    
	    assertEquals(true, service.deleteNode(nodeWithChild));	    
	    NodeBean deletedNode = service.findNodeByName("nodeWithChild");	    
	    assertNull(deletedNode);

	    
	}
	
}

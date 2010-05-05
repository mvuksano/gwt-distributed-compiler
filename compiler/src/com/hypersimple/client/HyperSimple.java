package com.hypersimple.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HyperSimple implements EntryPoint {
	  
  public interface TestResources extends ClientBundle {
    @Source("sample.xml")
    TextResource xmlFileWithLongText();
  }
  
  TestResources resources = GWT.create(TestResources.class);
	  
  public void onModuleLoad() {
//	  Document doc = XMLParser.parse(resources.xmlFileWithLongText().getText());
//	  Node n = doc.getFirstChild().getFirstChild();
//	  String s = n.getNodeValue();
	  Button button = new Button("test");
	  LayoutPanel layoutPanel=new LayoutPanel();
	  RootPanel.get("foo").add(button);
	  RootLayoutPanel.get().add(layoutPanel);
	  RootLayoutPanel.get().remove(layoutPanel);
	  RootLayoutPanel.get().add(button);
	  RootLayoutPanel.get().add(button);
//	  RootPanel.get().clear();
//	  RootPanel.get().add(new Label("" + s.length()));
//	  RootPanel.get().add(new Label(s));
//	  RootPanel.get().add(new TestPanel());
  }
}
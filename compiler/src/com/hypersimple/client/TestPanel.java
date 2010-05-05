package com.hypersimple.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.Widget;

public class TestPanel extends Composite {

	private static TestPanelUiBinder uiBinder = GWT
			.create(TestPanelUiBinder.class);

	interface TestPanelUiBinder extends UiBinder<Widget, TestPanel> {
	}
	
	@UiField SubmitButton submitButton;

	public TestPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("submitButton")
	void handleClick(ClickEvent e) {
		Window.alert("test");
	}

}

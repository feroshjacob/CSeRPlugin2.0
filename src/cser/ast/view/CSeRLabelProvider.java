/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package cser.ast.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.common.HandleObject;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import cser.Activator;
import cser.core.CSeRClone;

public class CSeRLabelProvider extends LabelProvider  {

	
  private static Map<String, Image> images = new HashMap<String, Image>();

	public CSeRLabelProvider() {
		
	
	}
	


	@SuppressWarnings("unchecked")
	public String getText(Object obj) {
		if(obj instanceof CSeRClone) return getText((CSeRClone)obj);
		if(obj instanceof List){
			return "group ("+  ((List) obj).size() + ")";
		}
		return null;

	}
	@Override
	public Image getImage(Object obj) {
		if(obj instanceof CSeRClone) {
			CSeRClone clone = (CSeRClone) obj;
			if(clone.getParent()!=null)
			return loadImage("copy.gif");
			return  loadImage("newBook.gif");
		}
		return null;
	}
	
	
	private Image loadImage(String imageName){
		if(images.containsKey(imageName)) return images.get(imageName);
		Image image = Activator.getImageDescriptor(imageName).createImage();
		images.put(imageName, image);
		return image;
		
	}
      private String getText(CSeRClone clone){
    	  StringBuffer buffer = new  StringBuffer();
    	  buffer.append(clone.getFileName());
    	  return buffer.toString();
    	  
      }
	

}

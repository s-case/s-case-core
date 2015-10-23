/**
 * Copyright 2015 S-CASE Consortium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.scasefp7.eclipse.core.ui.preferences.internal;

/**
 * @author emaorli
 *
 */
public class DomainEntry {
	private String name = null;
	private DomainEntry[] children = null;
	private DomainEntry parent = null;
	private int	id;
	
	/**
	 * Create the object using supplied values.
	 * 
	 * @param id for the domain
	 * @param name of the domain
	 * @param children of the domain
	 */
	public DomainEntry (int id, String name, DomainEntry[] children) {
		setId(id);
		setName(name);
		setChildren(children);
	}

	/**
	 * Create the object using supplied values.
	 * 
	 * @param id of the domain
	 * @param name of the domain
	 */
	public DomainEntry (int id, String name) {
		setId(id);
		setName(name);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the children
	 */
	public DomainEntry[] getChildren() {
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(DomainEntry[] children) {
		this.children = children;
		
		for(int ix=0; ix<children.length; ix++) {
			children[ix].setParent(this);
		}
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the parent
	 */
	public DomainEntry getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(DomainEntry parent) {
		this.parent = parent;
	}

	/**
	 * @return true if the entry has children
	 */
	public boolean hasChildren() {
		return this.getChildren() != null && this.getChildren().length > 0;
	}
	
	
	
}

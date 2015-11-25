package eu.scasefp7.eclipse.core.ontologytoyamltools;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class representing a property of an operation.
 * 
 * @author themis
 */
public class OperationProperty {

	/**
	 * The name of the property (note that all fields are public in order to allow serializing using
	 * {@link org.yaml.snakeyaml.Yaml Yaml}).
	 */
	public String Name;

	/** The type of this property. */
	public String Type;

	/** A boolean denoting if this property is unique or not. */
	public boolean Unique;

	/** The nested types of this property. */
	public ArrayList<String> Types;

	/**
	 * Initializes this property given its name, type and nested types.
	 * 
	 * @param parameterName the name of this property.
	 * @param parameterType the type of this property.
	 * @param parameterTypeElements the nested types of this property.
	 */
	public OperationProperty(String parameterName, String parameterType, ArrayList<String> parameterTypeElements) {
		Name = parameterName;
		Type = parameterType;
		if (StringHelpers.isPrimitive(Type))
			Type = Type.substring(0, 1).toUpperCase() + Type.substring(1);
		Unique = !(Type.equals("Array"));
		Types = new ArrayList<String>();
		for (String parameterTypeElement : parameterTypeElements) {
			if (StringHelpers.isPrimitive(parameterTypeElement))
				Types.add(parameterTypeElement.substring(0, 1).toUpperCase() + parameterTypeElement.substring(1));
			else
				Types.add(parameterTypeElement);
		}
	}

	/**
	 * Returns a YAML representation of this property.
	 * 
	 * @return a YAML representation of this property.
	 */
	public String toYAMLString() {
		String all = "  Name: " + Name;
		all += "\n  Type: " + Type;
		all += "\n  TypeRef: " + Arrays.asList(Types).toString().replaceAll("^\\[|\\]$", "");
		all += "\n  Unique: " + Unique;
		return all;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Name == null) ? 0 : Name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OperationProperty other = (OperationProperty) obj;
		if (Name == null) {
			if (other.Name != null)
				return false;
		} else if (!Name.equals(other.Name))
			return false;
		return true;
	}

}

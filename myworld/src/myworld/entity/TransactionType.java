package myworld.entity;

/**
 * TransactionType entity
 * @author huang li
 * 2014.9.18
 */
public class TransactionType {
	private int typeId=0;
	private String typeName=null;
	
	public TransactionType(){}
	
	public TransactionType(int typeId, String typeName) {
		super();
		this.typeId = typeId;
		this.typeName = typeName;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	
}

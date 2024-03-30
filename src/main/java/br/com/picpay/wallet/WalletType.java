package br.com.picpay.wallet;

public enum WalletType {
	
	COMUM(1), LOJISTA(2);
	
	private int value;
	
	private WalletType(int type) {
		this.value = type;
	}
	
	public int getValue() {
		return this.value;
	}
	
    public WalletType of(Integer source) {
        for (WalletType type : WalletType.values()) {
            if (type.getValue() == source) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid value for WalletType: " + source);
    }

}

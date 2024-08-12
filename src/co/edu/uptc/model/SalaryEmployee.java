package co.edu.uptc.model;

public class SalaryEmployee {

	private long salaryGross;
	private long auxTransport;
	private long salaryNet;
	private long productivityBonus;
	private long learningBonus;

	public long getSalaryGross() {
		return salaryGross;
	}

	public void setSalaryGross(long salaryGross) {
		this.salaryGross = salaryGross;
	}

	public long getAuxTransport() {
		return auxTransport;
	}

	public void setAuxTransport(long auxTransport) {
		this.auxTransport = auxTransport;
	}

	public long getSalaryNet() {
		return salaryNet;
	}

	public void setSalaryNet(long salaryNet) {
		this.salaryNet = salaryNet;
	}

	public long getProductivityBonus() {
		return productivityBonus;
	}

	public void setProductivityBonus(long productivityBonus) {
		this.productivityBonus = productivityBonus;
	}

	public long getLearningBonus() {
		return learningBonus;
	}

	public void setLearningBonus(long learningBonus) {
		this.learningBonus = learningBonus;
	}

}

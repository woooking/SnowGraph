package exps.codepattern.code.ir.statement;

import exps.codepattern.code.cfg.basiccfg.BasicCFGRegularBlock;

public class IRLabel implements IRAbstractStatement {
	private int index;

	public IRLabel(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return String.format("Label %d", index);
	}

	@Override
	public BasicCFGRegularBlock buildCFG(BasicCFGRegularBlock block) {
		BasicCFGRegularBlock nextBlock = block.getCFG().createRegularBlock();
		block.setNext(nextBlock);
		block.getCFG().mapLabelBlock(this, nextBlock);
		return nextBlock;
	}

}

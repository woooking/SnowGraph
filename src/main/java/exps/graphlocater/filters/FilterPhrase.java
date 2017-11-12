package exps.graphlocater.filters;


import exps.graphlocater.wrapper.PhraseInfo;
import exps.graphlocater.wrapper.Proof;
import exps.graphlocater.wrapper.ProofType;

public class FilterPhrase {
	public static boolean filter(PhraseInfo phrase) {
		String text = phrase.getText();
		for (String stop_phrase : Rules.stop_phrases) {
			if (text.contains(stop_phrase)) {
				Proof proof = new Proof(ProofType.FAIL_STOP_PHRASES);
				proof.setEvidence(stop_phrase);
				phrase.addProof(proof);
				return false;
			}
		}
		for (String stop_phrase : Rules.qa_phrases) {
			if (text.contains(stop_phrase)) {
				Proof proof = new Proof(ProofType.FAIL_STOP_PHRASES);
				proof.setEvidence(stop_phrase);
				phrase.addProof(proof);
				return false;
			}
		}

		return true;
	}
}

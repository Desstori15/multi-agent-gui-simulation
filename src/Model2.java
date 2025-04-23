class Model2 {
    @Bind public int LL;
    @Bind public double[] twKI, twKS, KI, KS, INW, EKS, IMP, results;

    public void run() {
        if (KI == null || KS == null || INW == null || EKS == null || IMP == null) {
            throw new IllegalStateException("Data is not initialized for Model2.");
        }

        results = new double[LL];
        for (int i = 0; i < LL; i++) {
            results[i] = KI[i] * 0.4 + KS[i] * 0.2 + INW[i] * 0.2 + EKS[i] * 0.1 - IMP[i] * 0.1;
        }
    }
}

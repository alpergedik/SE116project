public class Transition {
        public final String symbol;
        public final State fromState;
        public final State toState;

        public Transition(String symbol, State fromState, State toState) {
            this.symbol = symbol.toUpperCase();
            this.fromState = fromState;
            this.toState = toState;
        }
    }

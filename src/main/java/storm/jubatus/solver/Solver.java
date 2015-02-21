package storm.jubatus.solver;

import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

import backtype.storm.tuple.TupleImpl;
import us.jubat.classifier.LabeledDatum;
import us.jubat.common.Datum;

import java.util.List;

/**
 * Created by sasakiumi on 2/21/15.
 */
public abstract class Solver {
    public enum Type {
        CLASSIFIER,
        REGRESSION,
        RECOMMENDER
    }

    protected String _host;
    protected int _port;
    protected String _name;
    protected int _timeoutSec;

//    abstract public void train(String label, Datum datum) throws UnsupportedOperationException;
//    abstract public Datum predict(String label, Datum datum) throws UnsupportedOperationException;
//    abstract public void train(double label, Datum datum) throws UnsupportedOperationException;
//    abstract public Datum predict(double label, Datum datum) throws UnsupportedOperationException;

    abstract public void train(Tuple tuple);
//    abstract public Tuple predict(Tuple tuple);

    public Datum tuple2datum(Tuple tuple) {
        Datum d = new Datum();
        Fields fields = tuple.getFields();
        for (String field : fields) {
            Object obj = tuple.getByteByField(field);
            if (obj instanceof String) {
                d.addString(field, (String) obj);
            } else if (obj instanceof Integer
                    || obj instanceof Double
                    || obj instanceof Float) {
                d.addNumber(field, (Double) obj);
            } else {
                d.addBinary(field, (byte[]) obj);
            }
        }
        return d;
    }
}

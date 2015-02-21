package storm.jubatus.solver;

import backtype.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.jubat.common.Datum;
import us.jubat.regression.RegressionClient;
import us.jubat.regression.ScoredDatum;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sasakiumi on 2/21/15.
 */
public class Regression extends Solver implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(Regression.class);

    private RegressionClient _client;

    /**
     * Regression model which uses Jubatus RegressionClient
     * @param host
     * @param port
     * @param name
     * @param timeoutSec
     */
    public Regression(String host, int port, String name, int timeoutSec) {
        this._host = host;
        this._port = port;
        this._name = name;
        this._timeoutSec = timeoutSec;
        try {
            _client = new RegressionClient(host, port, name, timeoutSec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Regression(String host, int port, String name) {
        this(host, port, name, 5);
    }

    private void update(double label, Datum datum) {
        ScoredDatum scoredDatum = new ScoredDatum();
        scoredDatum.score = (float)label;
        scoredDatum.data = datum;
        _client.train(Arrays.asList(scoredDatum));
    }

    private Datum estimate(double label, Datum datum) {
        List<Float> ret = _client.estimate(Arrays.asList(datum));
        return new Datum().addNumber("score", ret.get(0));
    }


    private void train(double score, Datum datum) throws UnsupportedOperationException {
        update(score, datum);
    }

    private Datum predict(double score, Datum datum) throws UnsupportedOperationException {
        return estimate(score, datum);
    }

    /**
     * Training Regression model with given data
     * @param tuple
     */
    @Override
    public void train(Tuple tuple) {
        Datum d = tuple2datum(tuple);
        Double score = tuple.getDoubleByField("score");
        train(score, d);
    }

}

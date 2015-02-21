package storm.jubatus.solver;

import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import us.jubat.common.Datum;
import us.jubat.recommender.IdWithScore;
import us.jubat.recommender.RecommenderClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Recommender
 */
public class Recommender extends Solver implements Serializable{
    private static final Logger LOG = LoggerFactory.getLogger(Recommender.class);

    private RecommenderClient _client;

    /**
     * Recommender used Jubatus RecommenderClient for recommendation
     *
     * @param host
     * @param port
     * @param name
     * @param timeoutSec
     */
    public Recommender(String host, int port, String name, int timeoutSec) {
        this._host = host;
        this._port = port;
        this._name = name;
        this._timeoutSec = timeoutSec;
        try {
            _client = new RecommenderClient(host, port, name, timeoutSec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Recommender(String host, int port, String name) {
        this(host, port, name, 5);
    }

    private void update(String label, Datum datum) {
        _client.updateRow(label, datum);
    }

    private List<IdWithScore> predict(String label, int maxCount) {
        return _client.similarRowFromId(label, maxCount);
    }

    private List<IdWithScore> predict(Datum datum, int maxCount) {
        return _client.similarRowFromDatum(datum, maxCount);
    }

    /**
     * Train model with given data
     *
     * @param label
     * @param datum
     */
    public void train(String label, Datum datum) {
        update(label, datum);
    }

    /**
     * Give prediction answer from model
     * This receive one datum as seed argument
     * :TODO Should return multiple answeres
     *
     * @param label
     * @param datum
     * @return
     */
    public Datum predict(String label, Datum datum) {
        List<Datum> ret = new ArrayList<Datum>();
        List<IdWithScore> idWithScores = predict(datum, 1);
        for (IdWithScore idWithScore : idWithScores) {
            Datum d = new Datum();
            d.addString("id", idWithScore.id);
            d.addNumber("score", idWithScore.score);
            ret.add(d);
        }
        return ret.get(0);
    }

    public void train(double label, Datum datum) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public Datum predict(double label, Datum datum) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Training method with given tuple data
     * @param tuple
     */
    @Override
    public void train(Tuple tuple) {
        Datum d = tuple2datum(tuple);
        String label = tuple.getStringByField("label");
        train(label, d);
    }
}

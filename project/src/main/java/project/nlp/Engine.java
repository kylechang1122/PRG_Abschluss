package project.nlp;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.bson.Document;
import org.hucompute.textimager.fasttext.labelannotator.LabelAnnotatorDocker;
import org.hucompute.textimager.uima.gervader.GerVaderSentiment;
import org.hucompute.textimager.uima.spacy.SpaCyMultiTagger3;
import org.hucompute.textimager.uima.type.Sentiment;
import org.hucompute.textimager.uima.type.category.CategoryCoveredTagged;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

/**
 * Class that helps to perform the NLP Analysis
 */
public class Engine {

    private AnalysisEngine engine = null;

    public Engine() {
    }

    /**
     * Method that creates an AggregateBuilder
     *
     * @throws ResourceInitializationException Exception
     */
    public void createEngine() throws ResourceInitializationException {
        AggregateBuilder builder = new AggregateBuilder();
        URL posmap = Engine.class.getClassLoader().getResource("am_posmap.txt");


        builder.add(createEngineDescription(SpaCyMultiTagger3.class,
                SpaCyMultiTagger3.PARAM_REST_ENDPOINT, "http://spacy.lehre.texttechnologylab.org"
        ));

        builder.add(createEngineDescription(GerVaderSentiment.class,
                GerVaderSentiment.PARAM_REST_ENDPOINT, "http://gervader.lehre.texttechnologylab.org",
                GerVaderSentiment.PARAM_SELECTION, "text,de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence"
        ));

        assert posmap != null;
        builder.add(createEngineDescription(LabelAnnotatorDocker.class,
                LabelAnnotatorDocker.PARAM_FASTTEXT_K, 100,
                LabelAnnotatorDocker.PARAM_CUTOFF, false,
                LabelAnnotatorDocker.PARAM_SELECTION, "text",
                LabelAnnotatorDocker.PARAM_TAGS, "ddc3",
                LabelAnnotatorDocker.PARAM_USE_LEMMA, true,
                LabelAnnotatorDocker.PARAM_ADD_POS, true,
                LabelAnnotatorDocker.PARAM_POSMAP_LOCATION, posmap.getPath(),
                LabelAnnotatorDocker.PARAM_REMOVE_FUNCTIONWORDS, true,
                LabelAnnotatorDocker.PARAM_REMOVE_PUNCT, true,
                LabelAnnotatorDocker.PARAM_REST_ENDPOINT, "http://ddc.lehre.texttechnologylab.org"
        ));

        this.engine = builder.createAggregate();
    }

    /**
     * Method that performs the NLP Analysis
     *
     * @param id   Speech Id
     * @param text Speech Text
     * @return org.bson.Document
     * @throws UIMAException Exception
     * @throws IOException   Exception
     * @throws SAXException  Exception
     */
    public Document analyse(String id, String text) throws UIMAException, IOException, SAXException {
        JCas cas = this.toCas(text);

        SimplePipeline.runPipeline(cas, this.getEngine());

        System.out.println("Start serialize: ");

        serializeToXMI(cas, id);

        Document document = new Document();

        document.put("_id", id);
        document.put("Text", text);

        List<Annotation> dependencies = new ArrayList<>(JCasUtil.select(cas, Dependency.class));
        List<Document> depDocuments = annotationsToList(dependencies);
        document.put("Dependencies", depDocuments);

        List<Annotation> tokens = new ArrayList<>(JCasUtil.select(cas, Token.class));
        List<Document> tokDocuments = annotationsToList(tokens);
        document.put("Token", tokDocuments);

        List<Annotation> misc = JCasUtil.select(cas, NamedEntity.class).stream().filter(ne -> ne.getValue().equals("MISC")).collect(Collectors.toList());
        List<Document> miscDocuments = annotationsToList(misc);
        document.put("MISC", miscDocuments);

        List<Annotation> per = JCasUtil.select(cas, NamedEntity.class).stream().filter(ne -> ne.getValue().equals("PER")).collect(Collectors.toList());
        List<Document> perDocuments = annotationsToList(per);
        document.put("PER", perDocuments);

        List<Annotation> org = JCasUtil.select(cas, NamedEntity.class).stream().filter(ne -> ne.getValue().equals("ORG")).collect(Collectors.toList());
        List<Document> orgDocuments = annotationsToList(org);
        document.put("ORG", orgDocuments);

        List<Annotation> loc = JCasUtil.select(cas, NamedEntity.class).stream().filter(ne -> ne.getValue().equals("LOC")).collect(Collectors.toList());
        List<Document> locDocuments = annotationsToList(loc);
        document.put("LOC", locDocuments);

        List<Annotation> sentences = new ArrayList<>(JCasUtil.select(cas, Sentence.class));
        List<Document> sentencesDocuments = annotationsToList(sentences);
        document.put("Sentence", sentencesDocuments);

        List<Annotation> lemmata = new ArrayList<>(JCasUtil.select(cas, Lemma.class));
        List<Document> lemDocuments = annotationsToList(lemmata);
        document.put("Lemmata", lemDocuments);

        List<Annotation> pos = new ArrayList<>(JCasUtil.select(cas, POS.class));
        List<Document> posDocuments = annotationsToList(pos);
        document.put("POS", posDocuments);

        List<Annotation> sentiment = new ArrayList<>(JCasUtil.select(cas, Sentiment.class));
        List<Document> sentimentDocuments = annotationsToList(sentiment);
        document.put("Sentiment", sentimentDocuments);

        List<Annotation> ddc = new ArrayList<>(JCasUtil.select(cas, CategoryCoveredTagged.class));
        List<Document> ddcDocuments = annotationsToList(ddc);
        document.put("DDC", ddcDocuments);

        cas.reset();


        return document;
    }

    /**
     * Method that serializes the JCas Objects into XMI Files.
     *
     * @param cas JCas Object
     * @param id  Speech Id
     * @throws IOException  Exception
     * @throws SAXException Exception
     */
    public void serializeToXMI(JCas cas, String id) throws IOException, SAXException {

        System.out.println("Type the path where you want to save the serialized files: ");
        String output = "C://Users//Invictus//Desktop//Serialized Protocols";
        XmiCasSerializer.serialize(cas.getCas(), Files.newOutputStream(Paths.get(output + "/" + id + ".xmi")));
        System.out.println(id + " ---> Serialized :)");
    }

    /**
     * Method that creates JCas
     *
     * @param text Speech Text
     * @return JCas Object
     * @throws UIMAException Exception
     */
    private JCas toCas(String text) throws UIMAException {
        return JCasFactory.createText(text, "de");
    }


    public AnalysisEngine getEngine() {
        return engine;
    }

    public Engine setEngine(AnalysisEngine engine) {
        this.engine = engine;
        return this;
    }

    /**
     * Method that adds the Annotations into an org.bson.Document.
     * <p>
     * This method has been taken from Mr. Abrami, Musterloesung 3
     *
     * @param annotations Collections of Annotations
     * @return Collection
     */
    public static List<Document> annotationsToList(List<Annotation> annotations) {

        List<Document> documents = new ArrayList<>(0);

        annotations.forEach(annotation -> {

            Document document = new Document();
            document.put("begin", annotation.getBegin());
            document.put("end", annotation.getEnd());

            if (annotation instanceof POS) {

                document.put("value", ((POS) annotation).getPosValue());
                document.put("type", annotation.getType().getShortName());

            } else if (annotation instanceof CategoryCoveredTagged) {

                CategoryCoveredTagged pTemp = (CategoryCoveredTagged) annotation;
                document.put("value", pTemp.getValue());
                document.put("score", pTemp.getScore());

            } else if (annotation instanceof Lemma) {

                Lemma pTemp = (Lemma) annotation;
                document.put("value", pTemp.getValue());
                POS p = JCasUtil.selectCovered(POS.class, annotation).get(0);

                if (p != null) {
                    document.put("pos", p.getType().getShortName());
                }

            } else if (annotation instanceof org.hucompute.textimager.uima.type.GerVaderSentiment) {

                org.hucompute.textimager.uima.type.GerVaderSentiment gerVaderSentiment = (org.hucompute.textimager.uima.type.GerVaderSentiment) annotation;
                document.put("value", gerVaderSentiment.getSentiment());
                document.put("subjectivity", gerVaderSentiment.getSubjectivity());
                document.put("positive", gerVaderSentiment.getPos());
                document.put("negative", gerVaderSentiment.getNeg());
                document.put("neutral", gerVaderSentiment.getNeu());

            } else {
                document.put("value", annotation.getCoveredText());
            }

            documents.add(document);

        });

        return documents;

    }
}

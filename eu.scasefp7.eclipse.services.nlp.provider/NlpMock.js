module.exports = function(app) {

    var getServiceRoot = function() {
        var result = {
            "module": "NLP Server",
            "description": "NLP Server of the EU-funded project S-CASE. See http://www.scasefp7.eu/",
            "_links": {
                "phrase": "http://nlp.scasefp7.eu:8010/nlpserver/phrase",
                "sentence": "http://nlp.scasefp7.eu:8010/nlpserver/sentence",
                "project": "http://nlp.scasefp7.eu:8010/nlpserver/project", 
                "question":"http://nlp.scasefp7.eu:8010/nlpserver/question"
            }
        };
        return result;
    };

    var getSentenceAnnotation = function() {
        var result = {
            "annotation_format": "ann",
            "annotations": [
                "R1 ActsOn Arg1:T2 Arg2:T3",
                "R2 HasProperty Arg1:T3 Arg2:T4",
                "T1 Action 6 9 let",
                "T2 Action 13 16 try",
                "T3 Theme 30 40 invocation",
                "T4 Property 22 29 service"
            ],
            "created_at": "2015-10-06T12:19Z",
            "sentence": "First let me try some service invocation."
        };

        return result;
    };

    var getQuestionAnnotation = function() {
        var result = {
            "annotation_format": "ann",
            "query_terms": [
                "R1 ActsOn Arg1:T2 Arg2:T3",
                "R2 HasProperty Arg1:T3 Arg2:T4",
                "T1 Action 6 9 let",
                "T2 Action 13 16 try",
                "T3 Theme 30 40 invocation",
                "T4 Property 22 29 service"
            ],
            "created_at": "2015-10-06T12:19Z",
            "question": "First let me try some service invocation."
        };

        return result;
    };



    app.get('/nlpserver', function(req, res) {
        res.status(200).send(JSON.stringify(getServiceRoot()));
    });

    app.post('/nlpserver/sentence', function(req, res) {
        res.status(200).send(JSON.stringify(getSentenceAnnotation()));
    });

    app.post('/nlpserver/question', function(req, res) {
        res.status(200).send(JSON.stringify(getQuestionAnnotation()));
    });

};
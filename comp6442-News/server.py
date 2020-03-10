from flask import Flask, request, jsonify
from newsapi import NewsApiClient

""" 
    Enquiring NEWS API from this localhost server
    @author Dawen
"""

app = Flask(__name__)

API_KEY ='3498f3618263477a88f516f01aa24d00'
newsapi = NewsApiClient(api_key=API_KEY)

# GET Topheadlines from News API based on category
@app.route("/top-headlines/<category>")
def get_topheadlines_from_newsAPI(category):
    print(category)
    top_headlines = newsapi.get_top_headlines( category='business',
                                          language='en',
                                          country='us')
    return top_headlines

# GET all English News sources from News API
@app.route("/sources")
def get_sources():
    print("in get_sources")
    sources = newsapi.get_sources(language='en')
    return sources

    
# running web app in local machine
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
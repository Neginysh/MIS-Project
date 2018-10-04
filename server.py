from flask import Flask, Response
from flask import request
from flask import jsonify
import json
#from flask_api import status

app = Flask(__name__)

import mysql.connector


@app.route('/saveAnswers', methods=['POST'])
def saveAnswers():
    if not request.json or not 'user' in request.json:
        return "error", 201
    cnx = mysql.connector.connect(user='root', password='', host='127.0.0.1', database='TrueOrLie')
    data = request.json#json.loads(jsonify(request.json))
    user = json.loads(data["user"])[0]
    mycursor = cnx.cursor()
    sql = "INSERT INTO user (userid,age,nickname) VALUES (%s, %s, %s)"
    val = (int(user["id"]),int(user["age"]),user["nickName"])
    mycursor.execute(sql, val)
    sql = "INSERT INTO answer (answerTime,avgSensorM,avgSensorX,avgSensorY,avgSensorZ,btnPressureListAvg,btnPressureListNo,btnPressureMax,etAnswer,etDuration,questionId,swipeButtonLieDuration,swipeButtonTruthDuration,testId,userid,isTruth) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"
    userId = mycursor.lastrowid
    answers = json.loads(data["answerFeatures"])
    for answer in answers:   
        
        val = (answer["answerTime"],answer["avgSensorM"],answer["avgSensorX"],answer["avgSensorY"],answer["avgSensorZ"],answer["btnPressureListAvg"],answer["btnPressureListNo"],answer["btnPressureMax"],answer["etAnswer"],answer["etDuration"],answer["questionId"],answer["swipeButtonLieDuration"],answer["swipeButtonTruthDuration"],answer["testId"],int(userId), answer["isTruth"])
        mycursor.execute(sql, val)
    cnx.commit()
    mycursor.close()
    cnx.close()
    return jsonify(request.json), 201

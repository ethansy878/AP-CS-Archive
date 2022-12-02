# Python Project - Comp Sci x Statistics
# Statistical Data Analyzer ft. coronavirus data
# By Dihan A, Charles P, Ethan S
'''
Note: You need a text file, "saves.txt", for this program to work!
This program normally consists of two python files: 
"functions.py" and "main.py"
The Colab version (this) is a combination of both.
'''

# ARCHIVE NOTE: API KEY AVAILABLE UPON REQUEST
# PROGRAM WILL NOT WORK PRIOR TO OBTAINING THE API KEY 
# AND PLACING IT IN headers DICTIONARY!

##### "functions.py" #####
import matplotlib.pyplot as plt
from sty import fg, rs
import urllib.request
import pandas as pd
import numpy as np
import statistics
import requests
import json


## Websites ##
def search(date, kind):
    url = "https://covid-19-statistics.p.rapidapi.com/reports/total"
    querystring = {"date": date}

    headers = {
        'x-rapidapi-key': "<!> Available upon request <!>",
        'x-rapidapi-host': "covid-19-statistics.p.rapidapi.com"
    }

    response = requests.request("GET",
                                url,
                                headers=headers,
                                params=querystring)
    response_data = response.json()

    cases_data = response_data["data"]["confirmed"]
    deaths_data = response_data["data"]["deaths"]
    recovered_data = response_data["data"]["recovered"]

    if kind.lower() == "cases":
        return cases_data
    elif kind.lower() == "deaths":
        return deaths_data
    elif kind.lower() == "recoveries":
        return recovered_data


## Entry Functions ##
def file_entry():
    # This function well read from saves.txt, allowing the user to re-analyze old data sets
    entryCount = int((len(readFile("saves.txt")) / 10))
    print(f"You have {entryCount} saved entries:")

    header_list = []
    for x in range(entryCount):
        header_list.append(1 + 10 * x)

    # Line         01, 11, 21, 31, 41
    # Entry number 1   2   3   4   5

    #for i in header_list:
    #  print("Entry #" + str(int((i + 9) / 10)) + ":", str(readFileLine("saves.txt", i))
    for num, header in enumerate(header_list):
        print(f'Entry #{num+1}: {readFileLine("saves.txt", header)}')

    entrySelect = int(
        input("What entry do you want to view? (Enter its number) "))
    print("")

    entrySelect = (entrySelect - 1) * 10 + 1
    for x in range(entrySelect, entrySelect + 8):
        print(readFileLine("saves.txt", x))

    return readFileLine(
        "saves.txt", entrySelect + 8
    )  # Extra space b/c indexing issues in the main file with the "dataset:" header and eval(file_entry()[8:-1])


def website_entry():
    # This function will gather data from a website through apis, organize, and return
    # Topic of data: Covid-19 cases/deaths/recoveries
    dateList = []
    valueList = []

    kind = input('Pick a dataset. (Enter "Cases", "Deaths", or "Recoveries") ')
    sdate = str(input("Enter the starting date in the format yyyy-mm-dd: "))
    edate = str(input("Enter the ending date in the format yyyy-mm-dd: "))

    date_generator = pd.date_range(start=sdate, end=edate)
    for date in date_generator:
        date = str(date).split()[0]
        dateList.append(date)

    for date in dateList:
        value = search(date, kind)  # crossing to another function
        valueList.append(value)
        web_in_progress(len(valueList), len(dateList), kind)

    return [valueList, sdate, edate, kind]


## Files ##
def readFile(filename):
    #from mr.g
    #reads the contents of filename and returns as a list
    #filename and running program must be in same folder
    file = open(filename, 'r')
    contents = file.read()
    file.close()
    contents = contents.splitlines()
    return contents


def readFileLine(filename, line):
    #from mr.g
    #returns requested line# from filename
    #note: uses actual line#, not Python indexing
    line -= 1
    contents = readFile(filename)
    return contents[line]


def writeFileLine(filename, line, contents):
    #from mr.g
    #writes 'contents' to line# from filename
    #note: uses actual line#, not Python indexing
    line -= 1
    contents = str(contents)
    data = readFile(filename)
    while len(data) < (line + 1):
        data.append("")
    data[line] = contents
    #print (data)
    file = open(filename, 'w')
    thing = ''
    file.write(thing)
    file.close()
    for each in range(0, len(data)):
        file = open(filename, 'a')
        thing = data[each]
        file.write(thing + '\n')
        file.close()


def format_date(date):
    df = date.split('-')[1] + '/' + date.split('-')[2] + '/' + date.split(
        '-')[0]
    return df
    # example use: print(format_date('2020-05-05'))


## Statistics ##
def iqr(lisst):
    lisst.sort()
    firsthalf = []
    secondhalf = []

    if len(lisst) % 2 == 1:
        del lisst[int(len(lisst) / 2 + 0.5)]

    for x in range(0, int(len(lisst) / 2)):
        firsthalf.append(lisst[x])
    for x in range(int(len(lisst) / 2), len(lisst)):
        secondhalf.append(lisst[x])
    return [statistics.median(firsthalf), statistics.median(secondhalf)]


def printStatistics(lisst):
    print("Data trends:")
    print("Mean:", statistics.mean(lisst))
    print("Median:", statistics.median(lisst))
    print("Standard Deviation:", statistics.stdev(lisst))
    print("Variance:", statistics.variance(lisst))


def writeStatistics(lisst):
    return f'''
mean: {statistics.mean(lisst)}
median: {statistics.median(lisst)}
standard deviation: {statistics.stdev(lisst)}
variance: {statistics.variance(lisst)}
q1: {iqr(lisst)[0]}
q2: {iqr(lisst)[1]}
IQR: {abs(iqr(lisst)[1]-iqr(lisst)[0])}
{lisst}\n \n'''


## Color Printing ##
def ColorPrintDec(r, g, b, message, bonus):
    #from mr.g
    if bonus == False:
        print(fg(r, g, b) + message + fg.rs)
    else:
        print(fg(r, g, b) + message + fg.rs, bonus)


def web_in_progress(now, max, kind):
    redMaxed = 1
    greenMaxed = 1
    blueMaxed = 1
    if kind.lower() == "cases":
        redMaxed = 255
    elif kind.lower() == "deaths":
        blueMaxed = 255
    elif kind.lower() == "recoveries":
        greenMaxed = 255

    percent = (now / max) * 100
    message = f"Fetching data... {round(percent,0)}%"
    ColorPrintDec(round(((percent * 2.55) * redMaxed)),
                  round(((percent * 2.55) * greenMaxed)),
                  round(((percent * 2.55) * blueMaxed)), message, False)

    if percent == 100:
        ColorPrintDec(255, 255, 255, "Done!", "\u2705")


##### "main.py" #####
#from functions import * # (COMMENTED OUT, THIS VERSION IS ONE WHOLE THING)
data = []  # The big list of data that will be altered/analyzed


def get_data(method):
    # This function decides which way we will get data
    # Calls other functions in "functions.py"
    validating = True
    while validating:

        if method == 1:
            print(
                '\nYou selected the file option. These are all your past entries in the "saves.txt" file \n'
            )
            checkData = eval(file_entry())
            validating = False

        elif method == 2:
            print(
                '\nYou selected the website option. The current website provides global data from the COVID-19 pandemic\n'
            )
            checkData = website_entry()

            for each in checkData[0]:  # data validation
                try:
                    each == int(each)
                    each == float(each)
                except ValueError:
                    print(
                        "Your data is not valid! Please try again. (ValueError)",
                        "\u26A0")
                except TypeError:
                    print(
                        "Your data is not valid! Please try again. (TypeError)",
                        "\u26A0")
                except IndexError:
                    print(
                        "Your data is not valid! Please try again. (IndexError)",
                        "\u26A0")
                else:
                    for i in checkData[0]:
                        i = int(i)
                    validating = False

    return checkData


# Main code!
print("If you are a guest viewer, you can click on the 'code' button on the top\nClick on graph.png to access your graph")

running = True
while running:
    choice = input(
        'What data will you analyze? (Enter "file" or "website") ').lower()
    if choice == "file":
        data = get_data(1)
        choice = input('Graph this entry? (Enter "yes" or leave blank) ').lower()
        if choice == "yes":
          plt.close('all')
          plt.plot(data)
          plt.savefig('graph.png')

    if choice == "website":
        data = get_data(2)
        printStatistics(data[0])
        # "two or more calls to a function"
        plt.close('all')
        plt.plot(data[0])
        plt.savefig('graph.png')
        saves = input(
            '\nWould you like to log this data? (Enter "yes" or leave blank) ')
        if saves == "yes":
            with open('saves.txt', 'a') as textfile:
                textfile.write(
                    f'{format_date(data[1])} - {format_date(data[2])}: {data[3]}'
                )
                textfile.write(writeStatistics(data[0]))

    choice = input(
        '\nAll done? (Enter "yes" to end the program or leave blank to analyze again) '
    ).lower()
    if choice == "yes":
        running = False
        print("Goodbye! Use this data to your benefit!")
        with open('saves.txt','w') as textfile:
          textfile.write('')
    else:
        running = True
        print("")
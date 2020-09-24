import os
import csv
import pandas as pd
import json

# List all files in a directory using os.listdir
basepath = os.path.dirname(__file__)

for file in os.listdir(basepath):
    csvFileWithPath = os.path.join(basepath, file)
    if os.path.isfile(csvFileWithPath) and file.endswith('.csv') :
        filename, file_extension = os.path.splitext( csvFileWithPath )

        jsonFile = open(os.path.join(basepath, filename) + '.json', 'w')

        reader = open(csvFileWithPath)
        reader = csv.DictReader(open(csvFileWithPath))

        jsonStr = '[\n'
        for row in reader:
            jsonStr += '\t{ "minecraft_id": "player_head",'
            jsonStr += ' \t"name": "' + row['name'] + '",'
            jsonStr += ' \t"texture": "' + row['texture'] + '",'
            jsonStr += ' \t"uses_max": 64'
            jsonStr += '\t},\n'

        jsonStr = jsonStr[:-2]
        jsonStr += '\n]'
        jsonFile.write(jsonStr)
        # for line in reader.readlines(): 
        #     print(line)









        # # Read data from file 'filename.csv' 
        # data = pd.read_csv(fileWithPath, iterator=True) 

        # entryStr = '['

        # for entry in data:
        #     entryStr += '{ "minecraft_id": "player_head",'
        #     entryStr += ' "texture": "' + entry['Texture'].astype(str) + '",'
        #     entryStr += ' "uses_max": 64'
        #     entryStr += '},'
        #     print(entryStr)
        #     print('entryStr')
        #     break
            





        # entryStr += ']'
        # print(entryStr)

        # json = json.loads( str(entryStr) )
        # print(json)

        # output = open(basepath + 'output.json', 'w')
        # output.write( str(json) )
        # print(json)
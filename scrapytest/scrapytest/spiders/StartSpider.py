import os
import time
from xml.etree.ElementTree import ElementTree, Element

def startSpider():

    tree = ElementTree()
    tree.parse('./SpiderControl.xml')
    PageNodes = tree.findall('PageFiles/PageFile')

    Config_file_list = []
    Frequency_list = []
    cmd_list = []
    nodeNum = 0
    for Node in PageNodes:
        Config_file_list += Node.findall('FilePath')
        Frequency_list += Node.findall('Frequency')
        cmd_list += Node.findall('cmd')
        nodeNum += 1

    """
    
    TODO: add multi-thread here

    """
    for i in cmd_list:
        print(i.text)
    for j in Frequency_list:
        print(j.text)
    for i in range(len(cmd_list)):
        print(i)
        if cmd_list[i].text != None:
            while True:
                os.system(cmd_list[i].text)
                time.sleep(int(Frequency_list[i].text))

if __name__ == "__main__":

    startSpider()
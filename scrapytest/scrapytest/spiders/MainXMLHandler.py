import xml.sax


class MainXMLHandler(xml.sax.ContentHandler):
    def __int__(self):
        self.CurrentData = ""
        self.FilePath = ""
        self.PageFile = ""

    def startElement(self, tag, attributes):
        self.CurrentData = tag
        if tag == "PageFile":
            PageID = attributes["id"]
            print("===========Page " + PageID + "==============")

    def endElement(self, tag):
        if self.CurrentData == "FilePath":
            print ("File path: " + self.FilePath)
        self.CurrentData = ""

    def characters(self, content):
        if self.CurrentData == "PageFile":
            self.PageFile = content
        elif self.CurrentData == "FilePath":
            self.FilePath = content



"""
if __name__ == "__main__":

    parser = xml.sax.make_parser()

    parser.setFeature(xml.sax.handler.feature_namespaces, 0)

    Handler = MainXMLHandler()
    parser.setContentHandler(Handler)

    parser.parse("./SpiderControl.xml")
"""

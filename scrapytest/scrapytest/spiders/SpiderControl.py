import sched
import os
import time


class SpiderControl(object):

    def __init__(self, ):

schedule = sched.scheduler (time.time, time.sleep)

def func():
    os.system('scrapy crawl ccgp')

def performl(inc):
    schedule.enter(inc, 0, performl, (inc, ))
    func()

def mymain():
    schedule.enter(0, 0, performl, (86400, ))


if __name__ == "__main__":

        mymain()
        schedule.run()

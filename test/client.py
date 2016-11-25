#! /usr/bin/env python
#encoding=utf-8
import sys
import time
import random

sys.path.append('./gen-py')

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

from tips import SearchTips
from tips.ttypes import *

# -------------------------------------------------------------------------- #

def doWork(host, port, cityid, query):
    try:
        socket = TSocket.TSocket(host, port)
        transport = TTransport.TFramedTransport(socket)
        protocol = TBinaryProtocol.TBinaryProtocol(transport)

        client = SearchTips.Client(protocol)
        transport.open()

        req = TipsReq()
        req.cityid = cityid
        req.query = query
        req.uuid = "1385972103083%d" % ( random.randint(0, 1000) )
        req.reqid = 1
        begin = time.time()
        res = client.GetTips(req)
        res = res.tipsList

        print (time.time() - begin)
        wordlist = []
        for item in res:
            wordlist.append(item.word.upper())

        transport.close()

        return wordlist
    except Thrift.TException, tx:
        print '%s' % (tx.message)

# -------------------------------------------------------------------------- #

if __name__ == '__main__':
    argc = len(sys.argv)
    if argc != 3:
        print "usage: ", sys.argv[0], "cityid query"
    else:
        cityid = int(sys.argv[1])
        query = sys.argv[2]
        try:
            #wordlist = doWork('dx-dataapp-search-tips-staging01', 8989, cityid, query)
            wordlist = doWork('dx-dataapp-search-smartbox-test01', 8989, cityid, query)

            #print "------------------------------------------------------------"
            for i in wordlist:
                print i, " ",
            #print

        except Thrift.TException, tx:
            print "%s" % (tx.message)

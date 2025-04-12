// Command
public class PlaceOrderHandler : IRequestHandler<PlaceOrderCommand, Guid>
{
    private readonly ApplicationDbContext _context;
    private readonly IEventPublisher _publisher;

    public PlaceOrderHandler(ApplicationDbContext context, IEventPublisher publisher)
    {
        _context = context;
        _publisher = publisher;
    }

    public async Task<Guid> Handle(PlaceOrderCommand request, CancellationToken cancellationToken)
    {
        var order = new Order(request.CustomerId, request.ProductIds);
        _context.Orders.Add(order);

        await _context.SaveChangesAsync(cancellationToken);

        await _publisher.Publish(new OrderPlacedEvent(order.Id, request.CustomerId));

        return order.Id;
    }
}

// Query
public class GetOrderHistoryHandler : IRequestHandler<GetOrderHistoryQuery, List<OrderHistoryDto>>
{
    private readonly OrderHistoryDbContext _readDb;

    public GetOrderHistoryHandler(OrderHistoryDbContext readDb)
    {
        _readDb = readDb;
    }

    public async Task<List<OrderHistoryDto>> Handle(GetOrderHistoryQuery request, CancellationToken cancellationToken)
    {
        return await _readDb.OrderHistories
            .Where(x => x.CustomerId == request.CustomerId)
            .ToListAsync(cancellationToken);
    }
}

// Command-Event Handler (to populate the Query Model or database)
public class OrderPlacedEventHandler : IEventHandler<OrderPlacedEvent>
{
    private readonly OrderHistoryDbContext _readDb;

    public async Task Handle(OrderPlacedEvent @event)
    {
        var view = new OrderHistoryDto
        {
            OrderId = @event.OrderId,
            CustomerId = @event.CustomerId,
            PlacedAt = DateTime.UtcNow
        };

        _readDb.OrderHistories.Add(view);
        await _readDb.SaveChangesAsync();
    }
}
